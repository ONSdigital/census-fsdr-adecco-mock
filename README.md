Adecco Mock

git clone https://github.com/ONSdigital/census-fsdr-adecco-mock.git
git checkout add-acceptance-tests-to-fsdr
git pull

./gradlew clean bootRun

http://localhost:5679/swagger-ui.html#/

mockController - 
/mock/enableLogger - record any records we post to the mock (this should be enabled)
/mock/disableLogger - dont record any records we post to the mock (when performance testing fsdr)
/mock/postReponse - add Adecco employee
/mock/reset - delete records in mock

fsdr-controller
/fsdr/getEmployee - get Adecco Employees JSON

adecco-controller
/adecco/{sql} - fsdr will call this to get records

mock-g-suite
/gsuite/messages/ - get all messages sent to mock g suite by fsdr
/gsuite/messages/{email} - get messages sent to mock g suite by fsdr for an email
/gsuite/messages/reset - delete records in mock
/gsuite/<other ones> -  fsdr will call these

mock-service-now 
/servicenow/messages/ - get all messages sent to mock g suite by fsdr
/servicenow/messages/{email} - get messages sent to mock g suite by fsdr for an email
/servicenow/messages/reset - delete records in mock
/servicenow/<other ones> -  fsdr will call these

mock-service-now 
/xma/messages/ - get all messages sent to mock g suite by fsdr
/xma/messages/{email} - get messages sent to mock g suite by fsdr for an email
/xma/messages/reset - delete records in mock
/xma/<other ones> -  fsdr will call these



## Declared Queues

Mock.Events(topic) - ACTION.RESPONSE.PRODUCER.SERVICE_NOW -> FFA.Events.Exchange
Mock.Events(topic) - ACTION.RESPONSE.PRODUCER.XMA         -> FFA.Events.Exchange
Mock.Events(topic) - SERVICE.GSUITE                       -> FFA.Events.Exchange
