function run() {
    const urls = ['https://www.quicksdk.com/','http://www.hssdk.com/product.html','http://www.6xsdk.com/','https://www.vlsdk.com/']

    if (urls.indexOf(document.URL)===-1){
        console.log('油猴脚本之：Title、Meta导出 => 地址不匹配')
        return
    }

    function f() {
        const title = document.title

        let arr = []
        arr.push({title:title})
        for (let element of document.getElementsByTagName('script')) {
            arr.push(element.src)
        }
        alert(JSON.stringify(arr))


        for (let element of document.getElementsByTagName('meta')) {
            let json = {}
            const attrs = element.getAttributeNames()
            attrs.forEach(attr => json[attr] = element[attr])
            arr.push(json)
        }

        var funDownload = function (content, filename) {
            // 创建隐藏的可下载链接
            var eleLink = document.createElement('a');
            eleLink.download = filename;
            eleLink.style.display = 'none';
            // 字符内容转变成blob地址
            var blob = new Blob([content]);
            eleLink.href = URL.createObjectURL(blob);
            // 触发点击
            document.body.appendChild(eleLink);
            eleLink.click();
            // 然后移除
            document.body.removeChild(eleLink);
        };

        funDownload(JSON.stringify(arr, null, 2),title+'.txt')
    }

    f()
}