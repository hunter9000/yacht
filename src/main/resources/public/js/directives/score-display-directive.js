
yachtApp.directive('scoreDisplay', function() {
    return {
        template: `<div class="row roll-score-wrapper">
                        <div class="score-display-label">{{label}}</div>
                        <div class="score-display-score">{{score}}</div>
                    </div>`,
        restrict: 'E',
        scope: {
            label: '@',
            score: '@'
        },
    }
});
