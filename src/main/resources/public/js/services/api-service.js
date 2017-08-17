
yachtApp.factory('APIService', function($window, $location, $http, $log) {

    var getHeaders = function() {
        return { headers: {'x-access-token': $window.localStorage['jwtToken']} };
    };
    var getStandardFailureCallback = function(response) {
        return function(response) {
            $log.error(response);
            $location.path('/error');
        };
    };

    var getSuccessCallbackWrapper = function(callback, method, uri, data) {
        return function(response) {
            $log.debug('Response from '+method+': ' + uri);
            if (data) {
                $log.debug('With data: ');
                $log.debug(data);
            }
            $log.debug(response);
            callback(response);
        };
    };

    var get = function(uri, successCallback) {
        $log.debug('GET: ' + uri);
        $http.get(uri, getHeaders())
        .then(getSuccessCallbackWrapper(successCallback, 'GET', uri), getStandardFailureCallback());
    };
    var post = function(uri, data, successCallback, failureCallback) {
        $log.debug('POST: ' + uri + ' DATA:');
        $log.debug(data);
        if (!failureCallback) {
            failureCallback = getStandardFailureCallback();
        }
        return $http.post(uri, data, getHeaders())
        .then(getSuccessCallbackWrapper(successCallback, 'POST', uri, data), failureCallback);
    };
    var put = function(uri, data, successCallback) {
        $log.debug('PUT: ' + uri + ' DATA:');
        $log.debug(data);
        return $http.put(uri, data, getHeaders())
        .then(getSuccessCallbackWrapper(successCallback, 'PUT', uri, data), getStandardFailureCallback());
    };
    var patch = function(uri, data, successCallback) {
        $log.debug('PATCH: ' + uri + ' DATA:');
        $log.debug(data);
        return $http.patch(uri, data, getHeaders())
        .then(getSuccessCallbackWrapper(successCallback, 'PATCH', uri, data), getStandardFailureCallback());
    };
    var deleteCall = function(uri, successCallback) {
        $log.debug('DELETE: ' + uri);
        return $http.delete(uri, getHeaders())
        .then(getSuccessCallbackWrapper(successCallback, 'DELETE', uri), getStandardFailureCallback());
    };

    return {
        // API CALLS
        authenticate: function(data, successCallback) {
            post('/api/authenticate/', data, successCallback);
        },
        getUser: function(userId, successCallback) {
            get('/api/users/'+userId+'/', successCallback);
        },
        getUsers: function(successCallback) {
            get('/api/users/', successCallback);
        },
        updateUser: function(userId, data, successCallback) {
            put('/api/users/'+userId+'/', data, successCallback);
        },
        createUser: function(data, successCallback) {
            post('/api/user/', data, successCallback);
        },
        getProfile: function(successCallback) {
            get('/api/profile/', successCallback);
        },
        editProfile: function(data, successCallback) {
            put('/api/profile/', data, successCallback);
        },
        getAllRoles: function(successCallback) {
            get('/api/roles/', successCallback);
        },

        // Characters
        getGames: function(successCallback) {
            get('/api/game/', successCallback);
        },
        getGame: function(gameId, successCallback) {
            get('/api/game/'+gameId+'/', successCallback);
        },
        // returns the id of the newly created game
        createGame: function(successCallback) {
            post('/api/game/', {}, successCallback);
        },

        deleteGame: function(gameId, successCallback) {
            deleteCall('/api/game/'+gameId+'/', successCallback);
        },

        reroll: function(gameId, dieRolls, successCallback) {
            put('/api/game/'+gameId+'/rolls/', dieRolls, successCallback);
        },

        placeScore: function(gameId, rollType, successCallback) {
            put('/api/game/'+gameId+'/score/', rollType, successCallback);
        }
		
    };
});