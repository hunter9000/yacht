
yachtApp.directive('gameSheet', function() {
    return {
        template: `<div class="container">
                        <roll-score ng-repeat="type in UPPER_SECTION" score="sheet.rollScores[type]" roll-type="{{type}}" score-callback="placeScore" />

                        <score-display label="Upper Subtotal" score="{{sheet.topSectionSubtotal}}"/>
                        <score-display label="Upper Bonus" score="{{sheet.hasTopSectionBonus ? 35 : 0}}"/>
                        <score-display label="Upper Total" score="{{sheet.topSectionScore}}"/>

                        <roll-score ng-repeat="type in LOWER_SECTION" score="sheet.rollScores[type]" roll-type="{{type}}" score-callback="placeScore" />

                        <score-display label="Lower Total" score="{{sheet.bottomSectionScore}}"/>
                        <score-display label="Total" score="{{sheet.totalScore}}"/>
                    </div>`,
        restrict: 'E',
        scope: {
            sheet: '=',
            updateCallback: '&',
        },
        controller: function(APIService, $scope, $routeParams) {
            $scope.UPPER_SECTION = ['ACES', 'TWOS', 'THREES', 'FOURS', 'FIVES', 'SIXES'];
            $scope.LOWER_SECTION = ['THREE_OF_A_KIND', 'FOUR_OF_A_KIND', 'FULL_HOUSE', 'SMALL_STRAIGHT', 'LARGE_STRAIGHT', 'YAHTZEE', 'CHANCE'];

            $scope.placeScore = function(rollType) {
                var request = {
                    'rollType': rollType
                };
                APIService.placeScore($routeParams.gameId, request, function(response) {
                    $scope.updateCallback()();
                });
            }
        }
    }
});
