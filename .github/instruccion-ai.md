# Instruccion how AI should be work with this project

The target of this project is for app that manage bought/payment with credit card, debit card, cash, credit\
It show graphs to show how spend money\
It try to help to give information about you spending money

## Arquitecture

This project work with Onion, so it join user case with UI and services.

## Modules

> App
>> This  module its a main module for angular project in this module contain how app its work in android mobiles, it has build.gradble, Android Manifest and Main Activity.\
>> In this module exist the route between interfaces, some interfaces and logic, this has old code that keep working.\
>> In this module when the project started it contain all logic services and UI but when it was migrated was created multiple modules to work.

> Credit
>> This module contain logic UI to show interface for credits made.

> CreditCardModule
>> This module contain logic UI to show interface for bought with credit cards.

> japl-android-finances-paid-module
>> This module contain logic UI to show interface for bought with cash or debit cards.

> japl-finances-iports
>> This contain logic for DTOs, Enums, inbound y outbound and util accross modules and core, it help to use user cases.

> japl-android-finances-inputs
>> This contain logic for deep navigate, right know it does not work well, the target its try to split logic for deep navigate between interfaces since app

> japl-android-finances-service
>> This contain logic for api to provide data from DB and response it. 
>> It use sql logic.\
>> It use outbound port for provide how consume it\
>> outbount port -> dao -> DB

> japl-android-finances-core
>> This contain logic for onion arquitecture, so it has user cases\
>> inputs port -> user case -> output port\
>> use a japl-android-iports for input port (consume by UI and provide by core) and output port (consume by core and provide by services)

> japl-android-graphs
>> This contain logic for how print graphics bar/round to show data

> about-module
>> This its a module show about how create this project and other for same company/developer it is another repository\
>> github.com/nano871022/japl-android-about-module\
>> this shared between multiples project for that its save in another respository and it has some logic for know where app implemented it for change how show the information.

> ui
>> This contain logic for generic UI that should be use for other modules\
>> Ex:\
>> Text component its a common for ui but its replace per FieldText component its more rich that simple text and contain structure logic of app.
>>> Components
>>>> AlertDialogs
>>>> Buttons
>>>> Cards
>>>> Carousel
>>>> DataTable
>>>> FiledCheckBox
>>>> FieldDataPicker
>>>> FieldSelect
>>>> FieldText
>>>> FieldView
>>>> FormFieldView
>>>> MoreOptionsDialog
>>>> PiecePieGraph
>>>> Popup

# How Develop

* Use module for target logic how its specify in module section.
* Use Compose for create UI and use custome component in UI module.
* Use kotlin and create a controller for UI, it has method for call to inputport (core module), and use logic for how manage UI.
* Create inputbound if it needed in iports module when there are not a user case for use
* Create outbound if it needed in iports module when there are not a service for use
* Create a DTO to comunicate inbount/outbound with core when there are not for use
* Create a enums when there are not for use
* Create Services when there are not for use
* in core modue you create user case for each one, it keep all import info how order, validation for insert or request for calcs of more
* in service moduel you use for get data from db or save data in db
* try to keep simple implementations
* Create new components if there are not any other you can not use.
* Not create a components or complex logic for UI it make dirty if you logic if complex use/create UI component.
* !important! check other logic in the module and try to create in the same way (name, structure, components, calls, name method, name field, injects and more) .
* when you need to navigate between interfaces it need use a deeplink for it exist in app module the package  putparams where it contain how get parameter of bundle using deeplink