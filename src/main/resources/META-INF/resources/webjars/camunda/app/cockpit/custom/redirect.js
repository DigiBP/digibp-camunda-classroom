const doFetch = window.fetch;
window.fetch = function() {
    if (arguments[0] === '/api/admin/auth/user/default/logout') {
        return doFetch.apply(this, arguments).then((response) => {
            if (response.ok === true) {
                window.location.replace("/");
            }
            return response;
        });
    } else {
        return doFetch.apply(this, arguments);
    }
}