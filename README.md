# DigiBP Camunda Template

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Deploy to Heroku](https://img.shields.io/badge/deploy%20to-Heroku-6762a6.svg?longCache=true)](https://heroku.com/deploy)

## Roles

| Process Role 	| Group     	        | User    	| Tasklist   	| Cockpit 	| Admin 	| Name            	|
|--------------	|-----------	        |---------	|------------	|---------	|-------	|-----------------	|
| Owner        	| owner     	        | giulia  	| -          	| READ      | -     	| Giulia Ricci    	|
| Manager      	| manager, initiator    | martina 	| READ, START 	| ALL     	| -     	| Martina Russo   	|
| Analyst      	| analyst   	        | sofia   	| -          	| READ      | -     	| Sofia Conti     	|
| Engineer     	| engineer  	        | chiara  	| READ, START  	| ALL     	| READ   	| Chiara Lombardi 	|
| Participant  	| initiator, assistant 	| beppe   	| READ, START   | -       	| -     	| Beppe Ferrari   	|
| Participant  	| worker, chef          | matteo  	| READ          | -       	| -     	| Matteo Alfonsi  	|
| Participant  	| worker, courier       | silvio  	| READ          | -       	| -     	| Silvio Esposito 	|

```json
{
  "users": [
    {
      "firstName": "Giulia",
      "groupIds": [
        {
          "groupId": "owner"
        }
      ],
      "lastName": "Ricci",
      "password": "password"
    },
    {
      "firstName": "Martina",
      "groupIds": [
        {
          "groupId": "manager"
        }
      ],
      "lastName": "Russo",
      "password": "password"
    },
    {
      "firstName": "Sofia",
      "groupIds": [
        {
          "groupId": "analyst"
        }
      ],
      "lastName": "Conti",
      "password": "password"
    },
    {
      "firstName": "Chiara",
      "groupIds": [
        {
          "groupId": "engineer"
        }
      ],
      "lastName": "Lombardi",
      "password": "password"
    },
    {
      "firstName": "Beppe",
      "groupIds": [
        {
          "groupId": "initiator"
        },
        {
          "groupId": "assistant"
        }
      ],
      "lastName": "Ferrari",
      "password": "password"
    },
    {
      "firstName": "Matteo",
      "groupIds": [
        {
          "groupId": "worker"
        },
        {
          "groupId": "chef"
        }
      ],
      "lastName": "Alfonsi",
      "password": "password"
    },
    {
      "firstName": "Silvio",
      "groupIds": [
        {
          "groupId": "worker"
        },
        {
          "groupId": "courier"
        }
      ],
      "lastName": "Esposito",
      "password": "password"
    }
  ]
}
```

## Maintainer
- [Digitalisation of Business Processes](https://github.com/digibp)

## License

- [Apache License, Version 2.0](https://github.com/DigiBP/digibp-archetype-camunda-boot/blob/master/LICENSE)