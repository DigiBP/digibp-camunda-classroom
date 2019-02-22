window.camAdminConf = {
    // // custom libraries and scripts loading and initialization,
    // // see: http://docs.camunda.org/guides/user-guide/#tasklist-customizing-custom-scripts
    customScripts: {
        // names of angular modules defined in your custom script files.
        // will be added to the 'cam.tasklist.custom' as dependencies
        ngDeps: ['ch.fhnw.classroom.redirect.module'],

        // RequireJS modules to load.
        deps: ['custom-ng-module'],

        // RequreJS path definitions
        paths: {
            'custom-ng-module': '../custom-ng-module/script'
        }
    }
    // 'runtimeActivityInstanceMetrics': {
    //   'display': true
    // },
    // 'historicActivityInstanceMetrics': {
    //   'adjustablePeriod': true,
    //   'period': {
    //     'unit': 'day'
    //   }
    // },
    // 'locales': {
    //   'availableLocales': ['en', 'de'],
    //   'fallbackLocale': 'en'
    // }
};