
/**
 * Pass options array that contains information about what to display, and what the filter option is:
 * scopeOptions = [{'label': 'hello', 'image': 'icon.png', 'filter': 'BODY'}];
 * The filter options selected are exposed in the filter array:
 * scopeFilter = [];
 */

yachtApp.directive('listFilter', function($compile, $interpolate) {
    return {
        restrict: 'E',
        scope: {
            options: '=',       // array of icons and filter names
            filter: '=',        // exposed array of filter options
        },
        template: '<button ng-click="selectAll()" >All</button> \
                    <toggle-button ng-repeat="option in options track by $index" data="option" on-change="setFilter()" /> \
                    <button ng-click="selectNone()" >None</button> ',
        controller:  function($scope) {
            $scope.setFilter = function() {
                // for each options, check if it's selected, add to array
                $scope.filter.length = 0;       // clear the array
                for (i=0; i<$scope.options.length; i++) {
                    if ($scope.options[i].selected) {
                        $scope.filter.push($scope.options[i].filter);
                    }
                }
            }

            $scope.selectAll = function() {
                console.log('select all!');

                $scope.options.forEach(function(currentValue, index, array) {
                    currentValue.selected = true;
                });

                $scope.setFilter();
            }

            // select all by default
            $scope.selectAll();

            $scope.selectNone = function() {
                console.log('select none!');

                $scope.options.forEach(function(currentValue, index, array) {
                    currentValue.selected = false;
                });

                $scope.setFilter();
            }
        },
    }
});
