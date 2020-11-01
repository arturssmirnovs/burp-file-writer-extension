# Hack Overviewer Burp Extension

Posts every URL to specified URL in settings..

With DomainName and FullURL

curl -d '{"domain":"101992-42.chat.api.drift.com","type":"4","params":"{access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3MzQ3ODYxNDgwIiwiY2xpZW50SWQiOiJmNnp1aXpkeWh4cm03ciIsInVzZXJJZFR5cGUiOiJMRUFEIiwic2NvcGUiOiJsZWFkIiwiaXNzIjoiMTAxOTkyIiwiZXhwIjoxNjM1NzgxNTg0LCJpYXQiOjE2MDQyNDU1ODR9.QEGfTIf01GgF1zVMq8v7f-LXDi1LzqxLp8oHA7ZVbg1BML7E7o7JGKB-lKMnm-N4EcLv_fpAG7e5Ads7mlFdKA}","method":"POST", "url":"https://101992-42.chat.api.drift.com:443/api/auth", "hash":""}' -H "Content-Type: application/json" -X POST http://localhost/test.php

curl -d '{"domain":"presence.api.drift.com","type":"0","params":"{}","method":"OPTIONS", "url":"https://presence.api.drift.com:443/api/auth", "hash":""}' -H "Content-Type: application/json" -X POST http://localhost/test.php
