import { timer } from 'rxjs';


export async function getElementWait(getFunc: () => NodeList | HTMLCollection, min_dur_time_ms: number): Promise<NodeList | HTMLCollection> {
    let el = 0;
    const iv = 100;

    while(el <= min_dur_time_ms) {
        let r = getFunc();
        if (r != null && r.length > 0) return r;

        await timer(iv).toPromise();
        el += iv;
    }

    return null;
}

