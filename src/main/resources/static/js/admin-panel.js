function openPage(pageName, element) {
    var i, content, btns;
    content = document.getElementsByClassName("admin-action-content");
    for (i = 0; i < content.length; i++) {
        content[i].style.display = "none";
    }
    btns = document.getElementsByClassName("btn");
    for (i = 0; i < btns.length; i++) {
        btns[i].classList.remove("sidebar-active");
    }
    document.getElementById(pageName).style.display = "block";
    element.classList.add("sidebar-active");
}

// Get the element with id="default" and click on it
document.getElementById("default").click();