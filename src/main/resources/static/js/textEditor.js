const textarea = document.getElementById('textarea');
alert("dd");
function applyTextStyle(command) {
    document.execCommand(command, false, null);
}

function submitText(){
    const text = document.getElementById('editor').innerHTML;
    document.getElementById('text').value = text;
	console.log("text");
}