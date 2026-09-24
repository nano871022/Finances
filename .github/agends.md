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

## Arquitecture

* create layout.xml in app module in resource/layout folder
* add view/layout in navitate.xml in resource/navigate folder
* add view reference in menu/menu_setting in resource/menu folder
  * view reference connection between menu and navigation
  * this should be add for list view or indication of develop about it
* view list/form should be added in feature module
  * view list when you can show multiples records in same interface in a list
  * view form when you can edit or create record 
* user cases in core module, adapters inbound implementation, contain module to bind for inject interface with implementation
* services - connection to DB or other , adapter outboud implementation, contain module to bind for inject interface with implementation
* iport module - contain dto, inbount and outbound adaptrs

## Navigate

For navigate between views/fragments use a deeplink, its setting in navigation.xml in resources of app module, this file contain all fragment/view in in has a deeplink tag to know path to use, 
for use it need to create a string with navigate path how string on string.xml resource in feature module, in this you can add parameters to pass look like url parameters, used %s to pass it,
you use this string resource in a class into navigations in each moduel have one, this has a class specific for this navigation, in which you set a navController to use navigate, and properties if needed
for get parameters in destination view you need to create a class into params package, in which you set it to receive a SaveStateHandled(depends in you use viewModel() inject) object or bundle(when usea Bundle way move it to SavedStateHandled use), in this create a method called download in which return a map with differents parameters expected on it and use a deeplink for get data
Ex SaveStateHandled
"""
savedStateHandle.get<Intent>(Params.PARAM_DEEPLINK)?.let { intent ->
val uri = intent.dataString?.toUri()
val date = uri?.getQueryParameter("last_date")?.let{ DateUtils.toLocalDate(it) }
return mapOf<String,Any>(
"CREDIT_CODE" to (uri?.getQueryParameter("credit_code")?.toLongOrNull()?:0.toLong()),
"CODE" to (uri?.getQueryParameter("code")?.toLongOrNull()?:0.toLong()),
"LAST_DATE" to (date?: LocalDate.now())
)
}
"""
Ex Bundle (Migrated to SaveStateHandled)
"""
argument.let {
if( it.containsKey(Params.PARAM_DEEPLINK) ){
val intent = (it.get(Params.PARAM_DEEPLINK) as Intent).dataString?.toUri()
val date = intent?.getQueryParameter("last_date")?.let{ DateUtils.toLocalDate(it) }
return@download mapOf<String,Any>(
"CREDIT_CODE" to (intent?.getQueryParameter("credit_code")?.toLong()?:0.toLong()),
"CODE" to (intent?.getQueryParameter("code")?.toLong()?:0.toLong()),
"LAST_DATE" to (date?: LocalDate.now())
)
}else{
val code = it.getLong("CODE")
return@download mapOf<String,Any>(
"CODE" to code
)
}
}
"""

## Custom Components

Located: ui module
component: package

* FieldView: Component to does not modificate value, it put name of field in top of component in center of component(main part) put the value
* FieldViewCards: Component to does not modificate value, it put name(label) of field in left side and value and right side and it try to fix to label part so close
* FieldText: Component to modificate value, it put name of field in top of component in center of component(main part) put the value
* DataTable: Component to create a table with data information look like spreed sheet, does not able to modificate data
* LoadingProgress: Component to show 3 states
  * loading data , show loading progress and text with loading data info
  * text no data info when it does not foun any this to show - only if it needed
  * Composable view when data was found or not needed show a no data info
