*** Settings ***
Documentation    Verify country creation scenarios
Library          SeleniumLibrary
Resource         ../../auth.steps.robot
Resource         ../../selenium.utils.robot
Suite Setup      Before Test Suite
Suite Teardown   Close Browser
Test Setup       Before Test
Force Tags       country  logic

*** Test Cases ***
Create country with name in English (fill only mandatory fields)
	Input Text                        id=name  Germany
	Submit Form                       id=add-country-form
	Location Should Be                ${SITE_URL}/country/germany
	Element Text Should Be            id=page-header  Stamps of Germany
	Element Text Should Match Regexp  id=msg-success  Country has been added\.[\\n\\r]+Now you could proceed with creating series\.
	Go To                             ${SITE_URL}/series/add
	Country Field Should Have Option  Germany

Create country with name in English and Russian
	Input Text              id=name    Czechia
	Input Text              id=nameRu  Чехия
	Submit Form             id=add-country-form
	Location Should Be      ${SITE_URL}/country/czechia
	Element Text Should Be  id=page-header  Stamps of Czechia
	Go To                   ${SITE_URL}/country/czechia?lang=ru
	Element Text Should Be  id=page-header  Марки страны Чехия

*** Keywords ***
Before Test Suite
	Open Browser                        ${SITE_URL}/account/auth  ${BROWSER}
	Register Keyword To Run On Failure  Log Source
	Log In As                           login=coder  password=test

Before Test
	Go To  ${SITE_URL}/country/add
