class DevEditor {
    constructor(selector) {
        this.selector = document.getElementById(selector);
        this.tools = ["bold","italic","under_line"];
        this.createBoard();
    }
    //툴바 생성
    createBoard() {
        toolbar.id = "dev-editor-toolbar";
        // 에디터 상단에 툴바 추가
        const toolBar = this.#createTool();
        this.selector.parentNode.insertBefore(toolBar, this.selector);
    }
    setTool(tools){
        this.tools = tools;
    }
    #createTool(){
        const toolbar = document.createElement("div");
        this.tools.forEach(tool => {
            const toolBtn = document.createElement("button");
            toolBtn.classList.add("tool_btn");
            switch (tool) {
                case "bold":
                    toolBtn.textContent = "B";
                    toolbar.appendChild(toolBtn);
                    break;
                case "italic":
                    toolBtn.textContent = "I";
                    toolbar.appendChild(toolBtn);
                    break;
                case "under_line":
                    toolBtn.textContent = "U";
                    toolbar.appendChild(toolBtn);
                    break;
                default:

                    break;
            }
        });
        return toolbar;
    }
}

// 전역 객체로 등록
window.DevEditor = DevEditor;
