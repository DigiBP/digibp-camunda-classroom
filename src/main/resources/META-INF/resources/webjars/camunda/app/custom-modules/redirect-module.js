/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

let observer = new MutationObserver(() => {
    const logoutButton = document.querySelectorAll(".logout > a")[0];
    if (logoutButton) {
        logoutButton.setAttribute('href', '/camunda/app/');
        observer.disconnect();
    }
});

observer.observe(document.body, {
    childList: true,
    subtree: true,
    attributes: false,
    characterData: false
});