const origin_open = XMLHttpRequest.prototype.open
XMLHttpRequest.prototype.open = function (...args) {
    this.method = args[0]
    this.url = args[1]
    origin_open.apply(this, args)
}

const origin_send = XMLHttpRequest.prototype.send
XMLHttpRequest.prototype.send = function (...args) {
    this.data = args[0]
    origin_send.apply(this, args)
}

var accessor = Object.getOwnPropertyDescriptor(XMLHttpRequest.prototype, 'responseText');
Object.defineProperty(XMLHttpRequest.prototype, 'responseText', {
    get: function () {
        const responseText = accessor.get.call(this)
        const content = this.method + "\t" + this.url + "\t" + this.status + "\t" + responseText
        console.log(content)
        return responseText
    },
    set: function (val) {
        this.responseText = val
    }
})