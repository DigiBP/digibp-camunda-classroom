window.camTasklistConf = {
    // change the app name and vendor
    // app: {
    //   name: 'Todos',
    //   vendor: 'Company'
    // },
    //
    // configure the date format
    // "dateFormat": {
    //   "normal": "LLL",
    //   "long":   "LLLL"
    // },
    //
    // "locales": {
    //    "availableLocales": ["en", "de"],
    //    "fallbackLocale": "en"
    //  },
    //
    // // custom libraries and scripts loading and initialization,
    // // see: http://docs.camunda.org/guides/user-guide/#tasklist-customizing-custom-scripts
    customScripts: {
        // names of angular modules defined in your custom script files.
        // will be added to the 'cam.tasklist.custom' as dependencies
        ngDeps: ['classroom.redirect.module'],

        // RequireJS modules to load.
        deps: ['classroom-redirect-module','loadJS','bootstrap-switch'],

        // RequireJS path definitions
        paths: {
            'classroom-redirect-module': '../custom-modules/redirect-module',
            'loadJS': 'scripts/loadJS',
            'bootstrap-switch': 'scripts/bootstrap-switch.min'
        }
    },

    'shortcuts': {
        'claimTask': {
            'key': 'ctrl+alt+c',
            'description': 'claims the currently selected task'
        },
        'focusFilter': {
            'key': 'ctrl+shift+f',
            'description': 'set the focus to the first filter in the list'
        },
        'focusList': {
            'key': 'ctrl+alt+l',
            'description': 'sets the focus to the first task in the list'
        },
        'focusForm': {
            'key': 'ctrl+alt+f',
            'description': 'sets the focus to the first input field in a task form'
        },
        'startProcess': {
            'key': 'ctrl+alt+p',
            'description': 'opens the start process modal dialog'
        }
    }
};