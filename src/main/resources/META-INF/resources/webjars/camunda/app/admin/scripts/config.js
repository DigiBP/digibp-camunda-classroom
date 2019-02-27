window.camAdminConf = {
    // // custom libraries and scripts loading and initialization,
    // // see: http://docs.camunda.org/guides/user-guide/#tasklist-customizing-custom-scripts
    customScripts: {
        // names of angular modules defined in your custom script files.
        // will be added to the 'cam.tasklist.custom' as dependencies
        ngDeps: ['classroom.redirect.module'],

        // RequireJS modules to load.
        deps: ['classroom-redirect-module'],

        // RequreJS path definitions
        paths: {
            'classroom-redirect-module': '../custom-modules/redirect-module'
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