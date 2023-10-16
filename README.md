## What is Blackbox?

A lightweight framework that includes a set of architecture tools and approaches for building scalable, fully multiplatform applications (iOS, Android, Desktop) by leveraging Compose Multiplatform.

## Motivation
The primary objective of the framework is to ensure scalability of the project, providing high reusability of code hierarchies by decoupling them from each other. This can be especially beneficial for a large projects to support parallel development by different teams. For small projects or MVP’s, the framework provides tools, facilitating rapid development by enabling such projects to be scale-ready right from the start.
<br/>

This is achieved by breaking the application logic down into separate independent parts called black-boxes. Each black-box has a simple and strictly defined interface that hides the implementation from external context. Each black-box can be seen as a sub-application that may be used separately from the app context.

To learn more about concepts, [refer to wiki documentation.](https://github.com/trueangle/Blackbox/wiki/Concept)

<img src="https://github.com/trueangle/Blackbox/blob/master/wiki/img/black-box-intro-tree.png" alt="The Blackbox app at a high level" width="800"/>


## Features

1. Scalability by breaking an application down into independent components (aka **black-boxes**) with a simple interface that hides implementation details from the outer context. 
2. High reusability of the code hierarchies: feature modules, navigation flows and views. 
3. Easy project modularisation. Black boxes can be easily cohered into independent feature modules within a specific domain. The modules can be easily integrated into existing projects.
5. Fully multiplatform UI and business logic, no platform-specific knowledge is required. Supported targets: Android, iOS, Desktop, Web.
6. Lifecycle-aware components and navigation state preservation on Android & iOS.
7. Simple declarative navigation with deep links support, linear and modal navigation with unlimited levels of nesting.
8. Simple constructor based DI. Each DI module is tied to a scope of a black box and gets automatically destroyed when the component is no longer active.


## Benefits for Android developers

1. No iOS and Swift knowledge is required. 
2. 100% shared UI and business logic with minimum platform specificity.
3. Entirely Single Activity approach, without making use of multiple Fragment and Activities.
4. Lifecycle aware components and surviving configuration changes.
5. Edge-to-edge support.


## Benefits for iOS developers

1. Only basic Kotlin knowledge. No Android platform knowledge is required.  
2. Single UIViewController as an entry point. 
3. Scene based state preservation support.

## Setup
[Please refer to Setup wiki page.](https://github.com/trueangle/Blackbox/wiki/Setup)


## Sample app
To get familiar with the concept it is encouraged to referer to [sample/Movie](https://github.com/trueangle/Blackbox/tree/master/sample). The app contains typical use-cases implemented with respect to [Blackbox principles](https://github.com/trueangle/Blackbox/wiki/Concept). Such as: project modularisation, feature decoupling, complex navigation scenarios, complex Compose Multiplatform views and many more.


