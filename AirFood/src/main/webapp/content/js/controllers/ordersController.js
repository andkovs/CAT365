'use strict';

// Orders list

angular.module('myApp').controller('OrdersController', ['ordersService', 'dictionary', 'airports', 'orders', '$scope', 'fileUploadService',
    function (ordersService, dictionary, airports, orders, $scope, fileUploadService) {

        $scope.title = "Суточный план";
        //console.log(mainFactory.data);
        $scope.dictionary = dictionary.data;
        $scope.airports = airports.data;
        $scope.airports.unshift({id: 0, name: 'ALL'});
        $scope.currentAirport = 0;

        parseOrdersData(orders.data);

        //$scope.changeTime =  moment.utc(moment(now(),"DD-MM-YYYY HH:mm").diff(moment($scope.result.startTime,"DD-MM-YYYY HH:mm"))).format("HH:mm");

        $scope.changeTime = function (changeTime) {
            var response = '-';
            var now = moment();
            var change = moment(changeTime, 'DD-MM-YYYY HH:mm');
            var minutes = now.diff(change, 'minutes');
            if (minutes >= 1380) {
                return response;
            }
            if (minutes >= 60 && minutes < 1380) {
                return Math.floor(minutes / 60) + "ч.";
            }
            if (minutes < 60) {
                return minutes + "мин.";
            }
        };

        $scope.updateOrdersTime = function (from, to) {
            sessionStorage.setItem('from', from);
            sessionStorage.setItem('to', to);
        };

        $scope.getOrders = function () {
            var from = $scope.from;
            var to = $scope.to;
            $scope.updateOrdersTime(from, to);
            var getOrders = ordersService.get($scope.from, $scope.to);
            getOrders.then(function (response) {
                parseOrdersData(response.data);
            });
        };

        $scope.removeOrder = function (order) {
            if (confirm('Вы уверены, что хотите удалить заказ?')) {
                ordersService.deleteOrder(order).then(function (response) {
                    $scope.orders = _.without($scope.orders, order);
                }, function (response) {
                });
            }
        };

        function parseOrdersData(data) {
            $scope.from = data.from;
            $scope.to = data.to;
            $scope.orders = data.orders;
        };

        // Airport filter


        $scope.setCurrentAirport = function (id) {
            $scope.currentAirport = id;
        };

        // Order preview

        $scope.preview = function (id) {
            var getOrder = ordersService.getPreview(id);
            getOrder.then(function (response) {
                $scope.previewData = response.data;
                console.log('preview', $scope.previewData);
                $('#orderPreview').modal();
            });
        };

        $scope.getOrdersListExcel = function (from, to, airport) {
            ordersService.getOrdersExcel(from, to, airport);
        };

        $scope.getOrderPreviewExcel = function (id) {
            ordersService.getOrderExcel(id);
        };

        $scope.getFile = function (file) {
            fileUploadService.getPDFFile(file.id, $scope.previewData.id, file.name).then(function () {
            }, function () {
            });
        }

        $scope.removeFile = function (file) {
            if (confirm('Вы уверены, что хотите удалить аэропорт ' + file.name + '?')) {
                fileUploadService.deleteFile(file.id).then(function () {
                    $scope.order.files = _.without($scope.previewData.filesFrom, file);
                }, function () {
                    console.log('delete error', response);
                });
            }
        }

        $scope.uploadFile = function () {
            var file = $scope.myFile;
            var name = document.getElementById('fileUplFrom').files[0].name;
            fileUploadService.uploadFileToUrl(file, $scope.previewData.id, name, 'from').then(function (response) {
                var newFile = response.data;
                $scope.previewData.filesFrom.push(newFile);
                document.getElementById('fileUplFrom').value = "";
            }, function (response) {
                console.log('add error', response);
            });
            ;
        };

        // Date time picker

        var dtf = 'DD-MM-YYYY HH:mm';

        var defaultFrom = moment($scope.from, dtf);
        $('#dateTimeFrom').datetimepicker({format: dtf, defaultDate: defaultFrom});

        var defaultTo = moment($scope.to, dtf);
        $('#dateTimeTo').datetimepicker({format: dtf, defaultDate: defaultTo});

        $('#dateTimeFrom').on("dp.change", function (e) {
            $scope.from = $("#dateTimeFrom input").val();
            $('#dateTimeTo').data("DateTimePicker").minDate(e.date);
            $scope.$apply();
        });

        $('#dateTimeTo').on("dp.change", function (e) {
            $scope.to = $("#dateTimeTo input").val();
            $('#dateTimeFrom').data("DateTimePicker").maxDate(e.date);
            $scope.$apply();
        });


    }]);

// Order details

angular.module('myApp').controller('OrderDetailsController', ['airports', 'dictionary', 'order', '$state', '$scope', 'ordersService', 'fileUploadService',
    function (airports, dictionary, order, $state, $scope, ordersService, fileUploadService) {
        $scope.title = "Подробности заказа";
        $scope.order = order.data;
        $scope.dictionary = dictionary.data;
        $scope.airports = airports.data;
        $scope.airlines = $scope.dictionary.airlines;
        //$scope.airlines.unshift({id: 0, name: ''});
        $scope.copyOrder = order.data;
        $scope.flights = $scope.dictionary.flights;
        //$scope.flights.unshift({id: 0, number: ''});
        $scope.order.directComment = changeComment($scope.order.directComment);
        $scope.order.reverseComment = changeComment($scope.order.reverseComment);
        $scope.id = $scope.order.id;
        $scope.flightId = order.data.flightIdDirect;
        $scope.newOrder = {};
        $scope.currentAirline = bla($scope.flightId);

        function bla(flightId) {
            var resp = 0;
            angular.forEach($scope.dictionary.flights, function (item) {
                if (item.id == flightId) {
                    resp = item.airlineId;
                }
            });
            return resp;
        };

        function changeComment(comment) {
            if (comment == null) {
                comment = "";
            }
            return comment;
        }

        // $scope.setCurrentAirline = function(flightId) {
        // 	console.log('i am in setCurrentAirline', flightId);
        // 	$scope.currentAirline = 0;
        // 	angular.forEach($scope.flights, function (item) {
        // 		console.log('i in foreach', item);
        // 		if (item.id == flightId) {
        // 			$scope.currentAirline =  item.airlineId;
        // 			console.log('currentAirline', $scope.currentAirline);
        // 		}
        // 	});
        // };


        $scope.newRationDirectBusiness = {};
        $scope.newRationDirectEconom = {};
        $scope.newRationDirectCrew = {};
        $scope.newRationDirectSpecial = {};
        $scope.newRationReverseBusiness = {};
        $scope.newRationReverseEconom = {};
        $scope.newRationReverseCrew = {};
        $scope.newRationReverseSpecial = {};

        $scope.reset = function () {
            $scope.order = angular.copy($scope.copyOrder);
            $scope.currentAirline=bla($scope.copyOrder.flightIdDirect);
            // $('#dateTimeDeparture input').val("");
            if($scope.copyOrder.arriveDateTime==null){$('#dateTimeArrive input').val("");}
            if($scope.copyOrder.readyDateTime==null){$('#dateTimeReady input').val("");}
            if($scope.copyOrder.inspectionDateTime==null){$('#dateTimeInspection input').val("");}
            if($scope.copyOrder.workDateTime==null){$('#dateTimeWork input').val("");}
        };
        $scope.reset();

        $scope.addRation = function (ration, rationType, isDirect, orderId) {
            var newRation = {
                code: ration.code,
                amount: ration.amount
            };
            newRation.id = 0;
            newRation.classTypeId = rationType;
            if (isDirect == 1) {
                newRation.flight = "direct";
            } else {
                newRation.flight = "reverse";
            }
            newRation.orderId = orderId;
            if (isDirect == 1) {
                if (rationType == 1) {
                    $scope.order.rationDirectBusinessList.push(newRation);
                    $scope.newRationDirectBusiness = {};
                } else if (rationType == 2) {
                    $scope.order.rationDirectEconomList.push(newRation);
                    $scope.newRationDirectEconom = {};
                } else if (rationType == 3) {
                    $scope.order.rationDirectCrewList.push(newRation);
                    $scope.newRationDirectCrew = {};
                } else if (rationType == 4) {
                    $scope.order.rationDirectSpecialList.push(newRation);
                    $scope.newRationDirectSpecial = {};
                }
            }
            else {
                if (rationType == 1) {
                    $scope.order.rationReverseBusinessList.push(newRation);
                    $scope.newRationReverseBusiness = {};
                } else if (rationType == 2) {
                    $scope.order.rationReverseEconomList.push(newRation);
                    $scope.newRationReverseEconom = {};
                } else if (rationType == 3) {
                    $scope.order.rationReverseCrewList.push(newRation);
                    $scope.newRationReverseCrew = {};
                } else if (rationType == 4) {
                    $scope.order.rationReverseSpecialList.push(newRation);
                    $scope.newRationReverseSpecial = {};
                }
            }

        }

        $scope.removeRation = function (ration, rationType, isDirect) {
            if (isDirect == 1) {
                if (rationType == 1) {
                    console.log('hello')
                    $scope.order.rationDirectBusinessList = _.without($scope.order.rationDirectBusinessList, ration);
                } else if (rationType == 2) {
                    $scope.order.rationDirectEconomList = _.without($scope.order.rationDirectEconomList, ration);
                } else if (rationType == 3) {
                    $scope.order.rationDirectCrewList = _.without($scope.order.rationDirectCrewList, ration);
                } else if (rationType == 4) {
                    $scope.order.rationDirectSpecialList = _.without($scope.order.rationDirectSpecialList, ration);
                }
            }
            else {
                if (rationType == 1) {
                    $scope.order.rationReverseBusinessList = _.without($scope.order.rationReverseBusinessList, ration);
                } else if (rationType == 2) {
                    $scope.order.rationReverseEconomList = _.without($scope.order.rationReverseEconomList, ration);
                } else if (rationType == 3) {
                    $scope.order.rationReverseCrewList = _.without($scope.order.rationReverseCrewList, ration);
                } else if (rationType == 4) {
                    $scope.order.rationReverseSpecialList = _.without($scope.order.rationReverseSpecialList, ration);
                }
            }
            console.log('new ration in order', $scope.order)
        }

        $scope.addNewOrder = function (order) {
            if (order.drinkDirect != null) {
                order.drinkDirect.flight = "direct";
            }
            if (order.drinkReverse != null) {
                order.drinkReverse.flight = "reverse";
            }
            ordersService.postOrder(order).then(function (response) {
                alert('Все изменения успешно сохранены!');
                console.log('вот этот момент', response.data);
                $state.go('ordersDetails', {id: response.data});
            }, function (response) {
                console.log('add error', response);
            });
        };

        // Upload file

        $scope.uploadFile = function () {
            var file = $scope.myFile;
            var name = document.getElementById('fileUpl').files[0].name;
            fileUploadService.uploadFileToUrl(file, $scope.order.id, name, 'on').then(function (response) {
                var newFile = response.data;
                $scope.order.files.push(newFile);
                document.getElementById('fileUpl').value = "";
            }, function (response) {
                console.log('add error', response);
            });
            ;
        };

        $scope.removeFile = function (file) {
            if (confirm('Вы уверены, что хотите удалить файл ' + file.name + '?')) {
                fileUploadService.deleteFile(file.id).then(function () {
                    $scope.order.files = _.without($scope.order.files, file);
                }, function () {
                    console.log('delete error', response);
                });
            }
        }

        $scope.getFile = function (file) {
            fileUploadService.getPDFFile(file.id, $scope.order.id, file.name).then(function () {
            }, function () {
            });
        }

        // Date time picker

        var dtf = 'DD-MM-YYYY HH:mm';

        // if ($scope.order.departureDateTime == null) {
        //     $('#dateTimeDeparture').datetimepicker({format: dtf, defaultDate: null});
        // } else {
            var defaultDeparture = moment($scope.order.departureDateTime, dtf);
            $('#dateTimeDeparture').datetimepicker({format: dtf, defaultDate: defaultDeparture});
            $('#dateTimeDeparture').on("dp.change", function () {
                var test = $("#dateTimeDeparture input").val();
                console.log('то что мне нужно', test);
                $scope.order.departureDateTime = $("#dateTimeDeparture input").val();
                $scope.$apply();
            });
        // }

        if ($scope.order.arriveDateTime == null) {
            $('#dateTimeArrive').datetimepicker({format: dtf});
        } else {
            var defaultArrive = moment($scope.order.arriveDateTime, dtf);
            $('#dateTimeArrive').datetimepicker({format: dtf, defaultDate: defaultArrive});
        }
        $('#dateTimeArrive').on("dp.change", function () {
            console.log('то что мне нужно', $("#dateTimeArrive input").val());
            $scope.order.arriveDateTime = $("#dateTimeArrive input").val();
            $scope.$apply();
        });


        if ($scope.order.readyDateTime == null) {
            $('#dateTimeReady').datetimepicker({format: dtf});
        } else {
            var defaultReady = moment($scope.order.readyDateTime, dtf);
            $('#dateTimeReady').datetimepicker({format: dtf, defaultDate: defaultReady});
        }
        $('#dateTimeReady').on("dp.change", function () {
            $scope.order.readyDateTime = $("#dateTimeReady input").val();
            $scope.$apply();
        });

        if ($scope.order.inspectionDateTime == null) {
            $('#dateTimeInspection').datetimepicker({format: dtf});
        } else {
            var defaultInspection = moment($scope.order.inspectionDateTime, dtf);
            $('#dateTimeInspection').datetimepicker({format: dtf, defaultDate: defaultInspection});
        }
        $('#dateTimeInspection').on("dp.change", function () {
            $scope.order.inspectionDateTime = $("#dateTimeInspection input").val();
            $scope.$apply();
        });

        if ($scope.order.workDateTime == null) {
            $('#dateTimeWork').datetimepicker({format: dtf});
        } else {
            var defaultWork = moment($scope.order.workDateTime, dtf);
            $('#dateTimeWork').datetimepicker({format: dtf, defaultDate: defaultWork});
        }
        $('#dateTimeWork').on("dp.change", function () {
            $scope.order.workDateTime = $("#dateTimeWork input").val();
            $scope.$apply();
        });

        // Tabs

        $('#orderTabs').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        });

    }]);