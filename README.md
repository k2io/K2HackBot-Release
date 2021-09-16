# K2HackBot 


K2HackBot is an offering of K2 Cyber Security Inc, to help you secure your Web applications and APIs. K2HackBot tool will test if your Web application or APIs can be easily hacked. And will show you vulnerable url/APIs that can be hacked along with proof provided of one.

K2hackbot Currently Shipped in linux bundles with proposal for other system in near future 

Requirements
-------------
---
- Linux System
- Firefox

** K2Hackbot Provides Steps to install firefox on your machine for underprivileged user .\
In case of Root user Setup Script will try to install FireFox By itself

```shell
cd ${path_to_bundle};
./setup.sh or bash setup.sh;
```
** In case Firefox is not installed with provided steps then please make necessary changes and install Firefox

Installation
------------
---

Download Latest K2HackBot Bundle tar from
* https://github.com/k2io/K2HackBot-Release/releases/latest

Untar downloaded K2HackBot Bundle.

One of the suggested way is :
```shell
> tar -xf ${PATH_TO_K2HACKBOT_BUNDLE}
```

K2hackbot is shipped with all essential tools preinstalled .\
Use following command for setup 

```shell
> cd ${PATH_TO_UNTAR_K2HACKBOT_DIRECTORY};
> ./setup.sh or bash setup.sh;
```

script will provide an export command. Use the export command to set path for k2hackbot for the user 
.\

Instructions:
------------------

- Run following command to start K2HackBot:

```sh
k2hackbot [command] [options]
```

For help regarding supported Commands:

```sh
k2hackbot --help
```

For help regarding supported options of a command:

```sh
k2hackbot [command] --help
```

e.g.

```sh
k2hackbot scan-web-application --help
```

K2HackBot operates in following steps:

- Install K2agent: In this step, K2HackBot will try to install k2agent on your given host.
- K2crawling: In this step, K2HackBot will setup required environment and crawl application as authenticated or non-authenticated user.
- Extract Result: In this step, K2HackBot will generate report for Dynamic scan vulnerability for given application.

Supported config options:

- k2email - Email-id of K2 Portal(Optional)
- k2password - Password for K2 Portal Account(Optional)
- applicationUrl - list of URLs to be crawled by k2hackbot
- ignoreUrl - list of URLs to be ignored during Application Crawling

- isAuthRequired - If your application supports Authentication
- applicationLoginUrl - Login Page url of application(Only if authentication is required)
- applicationLoginIdentifier - JSON for application login identifier(Only if authentication is required). e.g.

```sh
{"username": {"identification": "user_field_id","value": "user_name"},"password": {"identification": "password_field_id","value": "password"},"submit": {"identification": "submit_button_id","value": "Nothing"}}
```

- remoteMachineCredentials - JSON for Remote Machine where application is running. e.g.

```sh
{"ip":"ip", "user":"user", "password":"password"}
```

- config - Provide the path of config file to be used for reading options instead of providing in CLI.

The Hierarchy for reading config is as follows:

```sh
CLI > Environment > Configuration file > Default
```

Examples:
-------------

K2HackBot command for applications required authentication for complete application crawling.

```sh
k2hackbot scan-web-application --applicationUrl "http://[ip]:[port]/app/login" -isauthreq true --applicationIdentifier "{\"containerid\":\"application_container_id\",\"pid\":application_pid}" --applicationLoginIdentifier "{\"username\": {\"identification\": \"type=\\\"text\\\"\", \"value\": \"john@acme.inc\"}, \"password\": {\"identification\": \"type=\\\"password\\\"\", \"value\": \"123456\"}, \"submit\": {\"identification\": \"type=\\\"submit\\\"\", \"value\": \"Login\"}}"
```



K2HackBot command for application without authentication

```sh
k2hackbot scan-web-application --applicationUrl "http://[ip]:[port]/app/login" -isauthreq false --applicationIdentifier "docker_container_id_or_application_pid"
```

K2HackBot command using config file:

```sh
k2hackbot scan-web-application --config path_of_config_file
```


