
yachtApp.directive('dieRoll', function() {
    return {
        template: `<div class="die-roll-wrapper" >
                        <toggle-button data="data" on-change="toggle()" />
                   </div>`,
        restrict: 'E',
        scope: {
            roll: '=',
        },
        controller: function($scope) {
            $scope.data = {
                'label': $scope.roll.value,
                'image': null,
                'filter': null,
                'selected': $scope.roll.marked};

            $scope.toggle = function() {
                $scope.roll.marked = $scope.data.selected;
            }
        }
    }
});


//                       <span>Value: {{roll.value}}</span>
//                       <input type="checkbox" /> Marked
//                       <span>Order: {{roll.order}}</span>