/** data has properties: label, image, filter, selected */

yachtApp.directive('toggleButton', function() {
    return {
        restrict: 'E',
        scope: {
            data: '=',
            onChange: '&',
        },
        template: '<button ng-click="toggle()" class="toggle-button" ng-class="{\'toggle-button-selected\': data.selected}"> \
                        <img src="{{data.image}}" ng-show="data.image" class="toggle-button-icon" /> \
                        <span ng-show="!data.image && data.label">{{data.label}}</span> \
                    </button>',
        controller: function($scope) {

            $scope.toggle = function() {
                console.log('toggling ' + $scope.data.label);
                $scope.data.selected = !$scope.data.selected;
                $scope.onChange();
            }

            $scope.getUI = function() {
                if ($scope.data.image) {
                    return '';
                }
            }
        },
    }
});