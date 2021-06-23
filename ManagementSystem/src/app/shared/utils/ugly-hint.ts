

const UglyHintSym    = Symbol('ugly-hint-text')
const inRed          = Symbol('in-red');
const originPosition = Symbol('origin-position');
const hintElemSym    = Symbol('hint-elem');
const errorMsgSym    = Symbol('error-msg');

function createNodeFromHtmlString(htmlText: string): HTMLElement {
    let div = document.createElement("div");
    div.innerHTML = htmlText.trim();
    return div.firstChild as HTMLElement;
}

function makeRed(input: HTMLInputElement, hintText: string) {
    if(input[inRed]) return;
    input[inRed] = true;
    input[originPosition] = input.parentElement.style.position;
    input.style.border = 'solid 1pt red';
    input.parentElement.style.position = 'relative';

    let hintElem = createNodeFromHtmlString(`<div 
        style='position: absolute;
               padding: 0.5em;
               top: -2.5em; 
               background: rgba(250, 200, 200, 0.6); 
               color: #555; 
               border-radius: 0.5em;
               white-space: nowrap;
               user-select: none;'>${hintText}</div>`);
    input[errorMsgSym] = hintText;
    input[hintElemSym] = hintElem;
    input.parentElement.appendChild(hintElem);
}
function cleanRed(input: HTMLInputElement) {
    if(!input[inRed]) return;
    input.style.border = '';
    input.parentElement.style.position = input[originPosition];
    input.parentElement.removeChild(input[hintElemSym]);
    input[hintElemSym] = null;
    input[inRed] = false;
    input[errorMsgSym] = null;
}

export function UglyInputHint(filter: (input: HTMLInputElement) => boolean, 
                              onblur: (input: HTMLInputElement) => string | null | Promise<string | null>,
                              oninput: (input: HTMLInputElement) => string | null | Promise<string | null> = onblur)
{
    const elems = document.getElementsByTagName("input");

    for (let i=0; i<elems.length; i++) {
        const item = elems.item(i) as HTMLInputElement;
        if (!filter(item)) continue;
        if (item == null || item[UglyHintSym] != null) continue;

        const handler = (handler: (input: HTMLInputElement) => string | null | Promise<string | null>, event: string) => {
            return async (ev: InputEvent) => {
                let input = ev.target as HTMLInputElement;
                let text = handler(input);

                if(text instanceof Promise) {
                    text = await text;
                }

                if(text) {
                    if (input[errorMsgSym] != text) {
                        cleanRed(input);
                    }
                    makeRed(input, text);
                } else if (event != 'blur') {
                    cleanRed(input);
                }
            }
        }
        item.addEventListener("input", handler(oninput, "input"));
        item.addEventListener("blur",  handler(onblur,  "blur"));
        item[UglyHintSym] = true;
    }
}

