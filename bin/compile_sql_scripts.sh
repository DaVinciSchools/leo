#!/bin/bash -x
cat ../database/src/main/resources/my-sql/*.sql > compiled_sql_scripts.sql
cat ../database/src/main/resources/my-sql-generated/*.sql >> compiled_sql_scripts.sql

