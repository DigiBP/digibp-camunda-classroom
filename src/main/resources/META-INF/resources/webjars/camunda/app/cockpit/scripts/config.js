export default {
    customScripts: [
        '../custom-modules/redirect-module.js',
        'scripts/definition-historic-activities.js',
        'scripts/instance-historic-activities.js',
        'scripts/instance-route-history.js',
        'scripts/tasklist-audit-log.js'
    ],
    bpmnJs: {
        additionalModules: [
            'scripts/robot-module.js'
        ],
    }
};