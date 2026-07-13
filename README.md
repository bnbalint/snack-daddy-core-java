# Overview

`snack-daddy-core` serves as the backend core for the SnackDaddy project. <br>

NOTE: This is the same as the Golang project - but it's written in Java/SpringBoot as a refresher on Java syntax.


## Database

Information on the database tables can be found in [docs/database.md](docs/frontend.md)


## Endpoints

An overview of endpoints can be found in [docs/endpoints.md](./docs/endpoints.md)

## Adding a new Rink or Level

Note that the options for Rinks and Levels are managed in enums **both** at the database level and the code level. <br>
To add a new Rink/Level, you would need to add a database migration, adding the value to the database enum and then also do a code release to update the enum in the code <br>.
Heavy handed? Maybe. But we would like to force data consistency, and nobody can spell BAIREL. <br>
Also, we don't *actually* expect these to ever change.

[Why both? - So that a manual manipulation of the database has forced consistency in addition to the application itself]



# Release Notes

## 1.0.0
- Date: 2026-06-30
- Changes
    - Start of basic file development