# automation
改进dk.brics.automaton.brics， 增加缺少的常用功能，能满足大多数使用正则表达式的要求

改进项
      增加 \w \W \d \D \h \H \v \V \s \S \r \n \t \f \a \e
      默认关闭 AUTOMATION, INTERVAL 选限， 避免 <> 必须转义 的要求
      增加 \p{字符类别}， \P{字符类别} 功能，兼容其他正则引擎
      增加 .*? 的非贪心匹配功能，兼容其他正则引擎
      修改内置自动机的加载方式，且默认加载全部内置自动机
      
