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
        redirect($window, $scope);
    }]);
    customModule.controller('NavigationController', ['$window', '$scope', function($window, $scope) {
        redirect($window, $scope);
    }]);

    return customModule;
});

function redirect ($window, $scope) {
    var isLoggedIn = true;
    var listener = $scope.$root.$on('authentication.changed', function(ev, auth) {
        if(!auth && isLoggedIn) {
            $window.location.href = $window.location.protocol + '//' + $window.location.hostname + ($window.location.port ? ':' + $window.location.port : '');
            isLoggedIn = false;
        }
    });

    $scope.$on('$destroy', function() {
        listener();
    });
}