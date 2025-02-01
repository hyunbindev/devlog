document.addEventListener('DOMContentLoaded', function() {
    getRepository("https://api.github.com/users/hyunbindev/repos");
});

function getRepository(repositoryURL){
    axios.get(repositoryURL)
    .then((res)=>{
        console.log(res.data);
        res.data.forEach(repo => {
            const repo_list_dom = document.getElementById('repo_list');
            addRepositoryComponent(repo , repo_list_dom);
        });
    });
}

function addRepositoryComponent(repoData , parentNode){
    
    const description = repoData.description;
    const url = `${repoData.url}/contents`;
    
    const repoComponent = document.createElement('div');
    repoComponent.textContent = description;

    repoComponent.addEventListener('click',()=>{
        axios.get(url).then((res)=>{
            res.data.forEach(subDir => {
                addSubDirComponent(subDir , repoComponent);
            })
        })
    })

    parentNode.appendChild(repoComponent);
}
function addSubDirComponent(compoData , parentNode){
    const dirComponent = document.createElement('div');
    dirComponent.textContent = compoData.name;
    //하위 디렉토리 생성
    if(compoData.type === 'dir'){
        parentNode.appendChild(dirComponent);
    }
}

function addFileComponent(){
    console.log("is file");
}