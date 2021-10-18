const menuMap =
    {
        "代码生成": {
            "Layui": {
                "生成表格": "html\\codeGenerator\\obj2Code\\arr2Table.html",
            },
            "Highcharts": {
                "生成清单": "html\\todo.html",
            }
        },
        "通用工具": {
            "json格式化": "html\\commUtil\\jsonFormat.html",
            "base64工具": "html\\commUtil\\base64.html"
        }
    }

function renderMenu(topbarId, sidebarId, iframeId) {
    const topbar = document.getElementById(topbarId)
    const sidebar = document.getElementById(sidebarId)

    let first;
    for (let topbarName in menuMap) {
        first = first === undefined ? topbarName : first
        topbar.innerHTML += `<li class="layui-nav-item layui-hide-xs"><a href="javascript:;">${topbarName}</a></li>`
    }
    renderSidebar(first, sidebar, iframeId);

    // var element = layui.element;从外面来
    element.on(`nav(${topbarId})`, elem => {
        renderSidebar(elem.context.innerText, sidebar, iframeId);
    });
    element.render('nav', topbarId)
}

function renderSidebar(topbarName, sidebar, iframeId) {
    const sidebarMap = menuMap[topbarName];
    sidebar.innerHTML = ''
    for (let sidebarName in sidebarMap) {
        if (sidebarMap[sidebarName] instanceof Object) {
            //有二级子菜单
            const nameAndUrl = sidebarMap[sidebarName]//{a: "", b: ""}
            let temp = ''
            for (const name in nameAndUrl) {
                temp += `<dd><a znLevel="2" znParent="${sidebarName}" znRoot="${topbarName}" href="javascript:;">${name}</a></dd>`
            }
            sidebar.innerHTML += `
                    <li class="layui-nav-item layui-nav-itemed">
                    <a znLevel="1" class="" href="javascript:;">${sidebarName}</a>
                    <dl class="layui-nav-child">`
                + temp
                + '</dl></li>'
        } else {
            //没有二级子菜单
            sidebar.innerHTML += `<li class="layui-nav-item"><a znLevel="2" znParent="${sidebarName}" znRoot="${topbarName}" href="javascript:;">${sidebarName}</a></li>`
        }
    }
    element.on(`nav(${sidebar.id})`, elem => {
        if (elem[0].getAttribute('znLevel') === '2') {
            const s = menuMap[elem[0].getAttribute('znRoot')]
                [elem[0].getAttribute('znParent')]
            if (s instanceof Object) {
                document.getElementById(iframeId).src = s[elem.context.innerText];
            } else {
                document.getElementById(iframeId).src = s;
            }
        }
    });
    element.render('nav', sidebar.id)
}
