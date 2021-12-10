const my_label = 'zn_query'

function get_my_label() {
    var my_label_obj = {}
    const node_list = document.querySelectorAll("*")
    for (const item of node_list) {
        if (is_not_blank(item[my_label])) {
            my_label_obj[item.id] = item[my_label]
        }
    }
    return my_label_obj
}

function is_blank(obj) {
    return obj === undefined || obj === null || obj === ''
}

function is_not_blank(obj) {
    return !is_blank(obj)
}

//获取字符串的字符长度
String.prototype.char_length = function () {
    let len = 0;
    for (let i = 0; i < this.length; i++) {
        if (this.charCodeAt(i) > 127 || this.charCodeAt(i) === 94) {
            len += 2;
        } else {
            len++;
        }
    }
    return len;
}

//判断长度并提示
String.prototype.validate_max_char_length = function (len, name) {
    if (is_blank(this)) return false
    if (this.char_length() > len) {
        throw new Error('[' + (name || this) + ']长度不能超过 ' + len);
    }
    return this
}

/**
 * @param data 数据集
 * @param mapping 对象元素映射code、name
 */
HTMLElement.prototype.select_bind_data = function (data, mapping) {
    const element = this

    element[my_label] = ''
    element.innerHTML = ''
    for (const idElement of data) {
        if (typeof idElement === 'object') {
            element.innerHTML += '<option value="' + idElement[mapping.code] + '">' + idElement[mapping.name] + '</option>';
        } else {
            element.innerHTML += '<option value="' + idElement + '">' + idElement + '</option>';
        }
    }
    element.onchange = function () {
        element[my_label] = this.options[this.selectedIndex].value
    }
    return null
}

var div_input_listen_hint_item = []
/**
 * @param mapping 对象元素映射code、name
 */
HTMLElement.prototype.div_input_listen_hint = function (mapping) {
    const element = this
    element[my_label] = ''
    //写入节点信息
    element.innerHTML = '<input id="' + element.id + '_input" style="width: 100%"/>' +
        '<div id="' + element.id + '_box" style="background-color: aqua;height: 10px;display: none"></div>'
    const _box = document.getElementById(element.id + '_box')
    const _input = document.getElementById(element.id + '_input')

    document.onclick = function () {
        _box.style.display = 'none'
    }

    function a() {
        _box.style.display = ''
        return false
    }

    _box.onclick = a()
    for (let item of document.getElementsByClassName('a')) {
        item.onclick = a()
    }
    // //点击body实现树形菜单隐藏
    // $(document).click(function () {
    //     $('#TreeDiv').hide();
    // });
    //
    // //点击OrgName文本框树形菜单不隐藏
    // $("#" + org_ele_id).click(function () {
    //     return false
    // });
    //
    // //点击树形菜单区域树形菜单不隐藏
    // $("#TreeDiv").click(function () {
    //     return false
    // });

    //输入内容监听
    _input.addEventListener("input", function () {
        console.log(this.value)
        // _box.style.display = ''
        //网络请求获取hint信息
        render_hint(['A', {a: 1, b: 'b'}])
    }, false);

    //渲染hint列表
    function render_hint(data) {
        div_input_listen_hint_item = data
        _box.style.display = ''
        _box.innerHTML = ''
        for (const idElement of data) {
            if (typeof idElement === 'object') {
                _box.innerHTML += '<div class="a" id="div_input_listen_hint_item_' + idElement[mapping.code] + '" style="cursor: pointer;" value="' + idElement[mapping.code] + '">' + idElement[mapping.name] + '</div>'
            } else {
                _box.innerHTML += '<div class="a" id="div_input_listen_hint_item_' + idElement + '" style="cursor: pointer;" value="' + idElement + '">' + idElement + '</div>'
            }
            document.getElementById('div_input_listen_hint_item_' + (typeof idElement === 'object' ? idElement[mapping.code] : idElement)).onclick = function () {
                _box.style.display = 'none'
                const code = typeof idElement === 'object' ? idElement[mapping.code] : idElement
                _input.value = typeof idElement === 'object' ? idElement[mapping.name] : idElement
                element[my_label] = code
                return null
            }
        }
    }

    return null
}