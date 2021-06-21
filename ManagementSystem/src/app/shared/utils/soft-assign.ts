
export function softAssignEnum(target: object, source: object) {
    if (typeof target !== 'object' || typeof source !== 'object') {
        console.error("fixme");
        return;
    }

    const sourceProps = Object.keys(source);
    const sourcePropSet = new Set(sourceProps);

    for (const prop of Object.keys(target)) {
        if (!sourcePropSet.has(prop)) {
            delete target[prop];
        }
    }

    for (const prop of sourceProps) {
        const s = source[prop];
        const t = target[prop];
        if (typeof s === 'object' && typeof t === 'object') {
            softAssignEnum(t, s);
        } else {
            target[prop] = s;
        }
    }
}

