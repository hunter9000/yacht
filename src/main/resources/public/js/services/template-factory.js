

yachtApp.factory('TemplateService', function ($http) {
//    var getTemplate = function (content) {
//        return $http.get('templates/' + content + '.html');
//    };
//
//    return {
//        getTemplate: getTemplate
//    };
    return {
        getTemplate: function (content) {
            return $http.get('pages/templates/' + content);
        }
    };
});