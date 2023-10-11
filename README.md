## What is Blackbox?

A lightweight framework that includes a set of architecture tools and approaches for building scalable, fully multiplatform applications (iOS, Android, Desktop, Web) by leveraging Compose Multiplatform.

<br/>

## What benefits Blackbox aims to deliver?

1. Scalability by breaking an application down into independent components (aka **black-boxes**) with a simple interface that hides implementation details from the outer context. 
2. High reusability of the code hierarchies: feature modules, navigation flows and views. 
3. Easy project modularisation. Black boxes can be easily cohered into independent feature modules within a specific domain. The modules can be easily integrated into existing projects.
5. Fully multiplatform UI and business logic, no platform-specific knowledge is required. Supported targets: Android, iOS, Desktop, Web.
6. Lifecycle-aware components and navigation state preservation on Android & iOS.
7. Simple declarative navigation with deep links support, linear and modal navigation with unlimited levels of nesting.
8. Simple constructor based DI. Each DI module is tied to a scope of a black box and gets automatically destroyed when the component is no longer active.

<br/>

## Benefits for Android developers

1. No iOS and Swift knowledge is required. 
2. 100% shared UI and business logic with minimum platform specificity.
3. Entirely Single Activity approach, without making use of multiple Fragment and Activities.
4. Lifecycle aware components and surviving configuration changes.
5. Edge-to-edge support.

<br/>

## Benefits for iOS developers

1. Only basic Kotlin knowledge. No Android platform knowledge is required.  
2. Single UIViewController as an entry point. 
3. Scene based state preservation support.
