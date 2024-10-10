# Update example claims after changing the json files.

Once one of the json files is changed, the example payloads should be updated in class `SendClaimsPayload.LIST_OF_CLAIMS`.
+ Perform build
+ Search for "Claims: " in the log output
+ Copy the claims and paste them in the class `SendClaimsPayload.LIST_OF_CLAIMS` (without surrounding brackets `[` and `]`)
+ Format json (e. g. with Visual Studio Code: `Alt` + `Shift` + `F`)
+ Copy and past into `LIST_OF_CLAIMS` 

`LIST_OF_CLAIMS` is then embedded automatically into the `SendClaimsPayload.EXAMPLE_PAYLOAD` and `GenerateClaimsPayload.EXAMPLE_RESPONSE` classes.

> **!!!** Dont forget to update the ID of the domain specific class in `SendClaimsPayload.EXAMPLE_PAYLOAD`! **IT MUST MATCH THE ID OF THE DataResource !!!** 