
export class Role {
    roleName: string;
    createdDate: Date;
    modifiedDate: Date;

    static fromObject(obj: object) //{
    {
        const ans = Object.create({}, Object.getOwnPropertyDescriptors(obj)) as Role;

        ans.createdDate = ans.createdDate && new Date(ans.createdDate);
        ans.modifiedDate = ans.modifiedDate && new Date(ans.modifiedDate);

        return ans;
    } //}
}

