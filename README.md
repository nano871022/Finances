[![Test](https://github.com/nano871022/Finances/actions/workflows/test.yml/badge.svg?branch=master)](https://github.com/nano871022/Finances/actions/workflows/test.yml)

-- alternative file but it will be load in another app
clave Nano871022.
alias finanzas 2.0


-- current file 
File file.pks
Clave Nano871022.
alias alejo87


graph TD
    A[finances Project] --> B[about-module Module]
    B --> C[ltl-java-android-about-module Local Folder]
    C --> D[Shared Code]
    D --> E[AboutActivity]
    D --> F[AboutFragment]
    D --> G[Resources: strings, layouts]
    D --> H[README.md]

    style A fill:#f9f,stroke:#333
    style B fill:#bbf,stroke:#333
    style C fill:#ff9,stroke:#333
    style D fill:#9ff,stroke:#333
    style E fill:#9ff,stroke:#333
    style F fill:#9ff,stroke:#333
    style G fill:#9ff,stroke:#333
    style H fill:#9ff,stroke:#333


graph TD
    A[finances Project] --> B[about-module Module]
    B --> C[ltl-java-android-about-module Local Folder]
    C --> D[Shared Code]
    D --> E[AboutActivity]
    D --> F[AboutFragment]
    D --> G[Resources: strings, layouts]
    D --> H[README.md]

    D --> I[Dependencies]
    I --> J[androidx.appcompat:appcompat:1.7.0]
    I --> K[com.google.android.material:material:1.10.0]
    I --> L[com.squareup.picasso:picasso:2.8.1]

    style A fill:#f9f,stroke:#333
    style B fill:#bbf,stroke:#333
    style C fill:#ff9,stroke:#333
    style D fill:#9ff,stroke:#333
    style E fill:#9ff,stroke:#333
    style F fill:#9ff,stroke:#333
    style G fill:#9ff,stroke:#333
    style H fill:#9ff,stroke:#333
    style I fill:#cfc,stroke:#333
    style J fill:#cfc,stroke:#333
    style K fill:#cfc,stroke:#333
    style L fill:#cfc,stroke:#333
