Meta:

Narrative:
As a user
I want to upload my file to Gdrive and than download it back
So that I do not lose important information

Scenario: Verify File is not corrupted after upload and download from Gdrive
Given authorized connection to Gdrive
When I upload <filename> to GDrive with <filesize>
And I download <filename> from GDrive
Then Downloaded file equals initial one
And Upload time took less than '15' seconds
And Download time tool less than '20' seconds

Examples:
|filename|filesize|
|textfile.txt|1MB|
|presentation.pptx|200MB|