
export function softAssignEnum(target: object, source: object) {
    if (typeof target !== 'object' || typeof source !== 'object') {
        console.error("fixme");
        return;
    }

    const sourceProps = Object.keys(source);
    const sourcePropSet = new Set(sourceProps);

    const deleteIdx: number[] = [];
    for (const prop of Object.keys(target)) {
        if (!sourcePropSet.has(prop)) {
            if (Array.isArray(target) && prop.match(/\d+/)) {
                deleteIdx.push(parseInt(prop));
            } else {
                delete target[prop];
            }
        }
    }

    deleteIdx.sort((a, b) => a >= b ? -1 : 1);
    if (Array.isArray(target)) {
        for (const i of deleteIdx) {
            target.splice(i, 1);
        }
    }

    for (const prop of sourceProps) {
        const s = source[prop];
        const t = target[prop];
        if (typeof s === 'object' && s !== null && 
            typeof t === 'object' && t !== null) 
        {
            softAssignEnum(t, s);
        }
        else {
            target[prop] = s;
        }
    }
}

