window.camCockpitConf = {
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
    // historicActivityInstanceMetrics: {
    //   adjustablePeriod: true,
    //   //select from the default time period: today, week, month, complete
    //   period: {
    //     unit: 'week'
    //   }
    // },
    // set if a user can change the default or no
    // userCanChangePeriod: true/false
    // },
    // 'locales': {
    //   'availableLocales': ['en', 'de'],
    //   'fallbackLocale': 'en'
    // },
    // skipCustomListeners: {
    //   default: true,
    //   hidden: false
    // },
    // 'batchOperation' : {
    //   // select mode of query for process instances or decision instances
    //   // possible values: filter, search
    //   'mode': 'filter'
    // },
    // bpmnJs: {
    //   moddleExtensions: {
    //     // if you have a folder called 'my-custom-moddle' (in the 'cockpit' folder)
    //     // with a file called 'camunda.json' in it defining the 'camunda' moddle extension
    //     camunda: 'my-custom-moddle/camunda'
    //   },
    //   additionalModules: {
    //     // if you have a folder called 'my-custom-module' (in the 'cockpit' folder)
    //     // with a file called 'redirect-module.js' in it defining the 'my-custom-module' AMD module
    //     myCustomModule: 'my-custom-module/module'
    //   }
    // }
};