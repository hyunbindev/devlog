let quill = new Quill('#editable',{
    theme: 'snow',
    placeholder:'새글을 작성해 보세요!',
    
   
    modules: {
        syntax: true,
        toolbar: '#toolbar'
    }
});
const test = document.getElementById('test');

test.addEventListener('click',()=>{
    const title = document.getElementById('title').value;
    const board = document.getElementById('boardDrop').value;
	indexMapping();
    const text = quill.getSemanticHTML();
    const innerElement = document.getElementById('editable').firstElementChild.childNodes;
    const thumbText = Array.from(innerElement).filter(element => !element.classList.contains('ql-code-block-container')).map(element => element.innerText).join(' ').substring(0, 100);
	
	axios.post('/manage/newpost',{
        title:title,
        board:{id:board},
        text:text,
        thumbText:thumbText
    }).then((res)=>{window.location.href = res.data.redirect})
    .catch((err)=>console.error(err));
})

function indexMapping(){
	const indexs = document.querySelectorAll("blockquote");
	indexs.forEach((index , i)=>{index.setAttribute("id",`index-${i+1}`)})
}




function updatePost(postid){
	const title = document.getElementById('title').value;
	const board = document.getElementById('boardDrop').value;
	indexMapping();
	const text = quill.getSemanticHTML();
	const innerElement = document.getElementById('editable').firstElementChild.childNodes;
	const thumbText = Array.from(innerElement).filter(element => !element.classList.contains('ql-code-block-container')).map(element => element.innerText).join(' ').substring(0, 100);

	axios.post('/manage/updatepost',{
	    id:postid,
		title:title,
	    board:{id:board},
	    text:text,
	    thumbText:thumbText
	}).then((res)=>{window.location.href = res.data.redirect})
	.catch((err)=>console.error(err));
}