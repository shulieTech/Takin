# instrument-modules
`instrument-modules`包含了所有用户的自定义模块，所有我们支持的中间件列表全都放在这个工程目录下。

## 工程介绍
### biz-classloader-inject

在一些特殊场景下需要将插件中自定义的对象注入到业务中，但是由于类加载的问题，如果直接在插件中直接将自定义的对象直接注入到业务中，则会存在找不到类的问题，但是也不能直接将实现暴露到 bootstrap中因为会存在直接引用业务类的情况，所以此时就需要一种特殊的做法可以将某些类直接注入到业务类加载器中，这个模块就是为了解决这个场景而设计的。
需要注意的是此模块不能与 simulator 及扩展插件存在直接的交互，交互都必须要通过 bootstrap 中暴露的 API 来完成(不然会存在类加载不到的问题)，这个模块下的子模块在打包时都会打到biz-classloader-jars目录下，另外还需要对应的加载配置，需要在 biz-classloader-inject.properties 里面配置触发的类，classname=jar 包称,
当指定的 classname 触发加载时则将对应 jar 包里面的内容全部注入到对应的类加载器中

### bootstrap-inject

这个模块下定义所有扩展模块需要向 bootstrap 中暴露的 API，注入到 boostrap 中则所有的类加载器都可访问。需要注意的是暴露到 bootstrap 中的 API 不能存在与 simulator 及扩展模块、业务类直接的引用(因为无法直接访问)，通常扩展模块需要向全局暴露一些能力可以通过在 bootstrap 模块中定义对应的钩子，由扩展模块实现并注入进去的方式

### user-modules

用户扩展模块，这个模块下面包含所有目前已经支持的中间件列表

## 插件开发指引

see [插件开发](./PluginDev.md)