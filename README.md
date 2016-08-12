Automation - 改进的BRICS                   
=====================================

What is Automation

brics 是一个高性能的自动机，包含了一个正则表达式引擎。
可惜， 这个正则表达式引擎有许多使用上的限制。
已知的限制如下：

	内置的名称与常用的字符冲突，导致很多在其它引擎中不需要转义的字符都要转义
	不支持常用的转义名称，如\w \s
	不支持非贪心匹配
	不支持捕获 group
	
ChangeLog

    增加 \w \W \d \D \h \H \v \V \s \S \r \n \t \f \a \e
    默认关闭 AUTOMATION, INTERVAL 选限， 避免 <> 必须转义 的要求
    增加 \p{字符类别}， \P{字符类别} 功能，兼容其他正则引擎
    增加 .*? 的非贪心匹配功能，兼容其他正则引擎
    修改内置自动机的加载方式，且默认加载全部内置自动机
	  
Acknowledgements

	tommyettinger/gwt-automaton