# dbgrep Tool

## About

## Development

### Start test environment

Prerequisites:

- docker installed
- Under Windows: wsl2 with docker installed

To start test environment execute following command in project root directory:

`docker compose up`

### Execute tests

Use following command to execute unit tests:

`mvn surefire:test`

Use following command to execute integration tests:

`mvn failsafe:integration-test`

### Check code quality

Use following command to check your code quality locally:

- `mvn -Pcicdprofile clean install checkstyle:check pmd:check spotbugs:check`

For separate execution:

- `mvn -Pcicdprofile install`
- `mvn -Pcicdprofile checkstyle:check`
- `mvn -Pcicdprofile pmd:check`
- `mvn -Pcicdprofile spotbugs:check`