/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

'use strict';
define('classroom-redirect-module', [
    'angular'
], function (angular) {
    var customModule = angular.module('classroom.redirect.module', []);

    customModule.run(function($window, $rootScope) {
        var isLoggedIn = true;
        var listener = $rootScope.$on('authentication.changed', function(ev, auth) {
            if(!auth && isLoggedIn) {
                $window.location.href = $window.location.protocol + '//' + $window.location.hostname + ($window.location.port ? ':' + $window.location.port : '');
                isLoggedIn = false;
            }
        });

        $rootScope.$on('$destroy', function() {
            listener();
        });
    });

    return customModule;
});