import { assert } from 'src/app/shared/utils';

export class PermMenu {
    permEntryName: string;
    link: string;
    descriptor: string;
    entryType: string;

    children: PermMenu[];
    enabled: boolean = true;

    private setEnableStatus(enabled: boolean, recursive: boolean) //{
    {
        this.enabled = enabled;
        if(recursive) {
            for(const child of this.children || []) {
                child.setEnableStatus(enabled, true);
            }
        }
    } //}
    enable(recursive?: boolean) {this.setEnableStatus(true, !!recursive);}
    disable(recursive?: boolean) {this.setEnableStatus(false, !!recursive);}

    get disabledRecursively(): boolean //{
    {
        if(this.enabled) return false;
        for(const child of this.children || []) {
            if (!child.disabledRecursively) {
                return false;
            }
        }
        return true;
    } //}
    get enabledRecursively(): boolean //{
    {
        if(!this.enabled) return false;
        for(const child of this.children || []) {
            if (!child.enabledRecursively) {
                return false;
            }
        }
        return true;
    } //}

    get baseDescriptor(): string {
        return this.descriptor && this.descriptor.split('.').pop();
    }

    static fromObject(obj: object): PermMenu {
        const objv = obj as PermMenu;
        const ans = Object.create(PermMenu.prototype, Object.getOwnPropertyDescriptors(objv)) as PermMenu;
        ans.children = [];
        for(const objs of objv.children || []) {
            ans.children.push(PermMenu.fromObject(objs));
        }

        return ans;
    }
}

