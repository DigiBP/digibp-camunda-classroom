/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

'use strict';
define('custom-ng-module', [
    'angular'
], function (angular) {
    // define a new angular module named my.custom.module
    // it will be added as angular module dependency to builtin 'cam.tasklist.custom' module
    // see the config.js entry above
    var customModule = angular.module('classroom.redirect.module', []);

    // …so now, you can safely add your controllers…
    customModule.controller('camHeaderViewsCtrl', ['$window', '$scope', function($window, $scope) {
        // isLoggedIn: flag set to true initially, since user is logged in
        var isLoggedIn = true;

        // register listener to 'authentication.changed' event
        var listener = $scope.$root.$on('authentication.changed', function(ev, auth) {
            if(!auth && isLoggedIn) {
                console.log('logout');
                $window.location.href = 'http://localhost:8080/';
                isLoggedIn = false;
            }
        });

        // remove listener to 'authentication.changed' event
        $scope.$on('$destroy', function() {
            listener();
        });

    }]);

    return customModule;
});

/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

'use strict';
define('custom-ng-module', [
    'angular'
], function (angular) {
    var customModule = angular.module('classroom.redirect.module', []);
    customModule.controller('camHeaderViewsCtrl', ['$window', '$scope', function($window, $scope) {
        var isLoggedIn = true;
        var listener = $scope.$root.$on('authentication.changed', function(ev, auth) {
            if(!auth && isLoggedIn) {
                //$window.location.href = $window.location.protocol + '//' + $window.location.hostname + ($window.location.port ? ':' + $window.location.port : '');
                location.href = {url: location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '')};
                isLoggedIn = false;
            }
        });

        $scope.$on('$destroy', function() {
            listener();
        });

    }]);

    return customModule;
});