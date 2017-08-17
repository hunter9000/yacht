
yachtApp.directive('rollScore', function() {
    return {
        template: `<div class="row roll-score-wrapper">
                        <div class="roll-score-type">{{score.rollType.name}}</div>
                        <!--<div class="roll-score-chosen">{{score.chosen}}</div>-->
                        <div class="roll-score-score">
                            <span class="light-text" ng-show="score.chosen">{{score.score}}</span>
                            <span ng-hide="score.chosen">{{score.potentialScore}}</span>
                        </div>
                        <div class="roll-score-button" >
                            <button class="btn btn-secondary" ng-hide="score.chosen" ng-click="scoreCallback()(rollType)">Score</button>
                        </div>
                    </div>`,
        restrict: 'E',
        scope: {
            score: '=',
            rollType: '@',
            scoreCallback: '&',
        },
    }
});
