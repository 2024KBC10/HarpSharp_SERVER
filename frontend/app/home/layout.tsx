import AIIcon from "../(common)/(component)/(ai)/ai_icon";
import FixedMenu from "../(common)/(component)/(menu)/fixed_menu";

export default function HomeLayout({
    children,
}: Readonly<{ children: React.ReactNode }>) {
    return (
        <>
            <FixedMenu/>
            <AIIcon/>
            {children}
        </>
    )
}