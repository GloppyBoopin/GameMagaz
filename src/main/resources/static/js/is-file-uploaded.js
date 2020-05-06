function showName () {
    var name = document.getElementById('file-upload');
    var text = document.getElementById('file-name');
    text.textContent = name.files.item(0).name;
}