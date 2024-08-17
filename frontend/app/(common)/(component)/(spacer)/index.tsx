export default function Spacer({
    spacing,
    align = "center",
    direction = "row",
}: SpacerProps) {
    return (
        <div
        style={{
            alignSelf: align,
            marginLeft: direction === "row" ? `${spacing}px` : "0px",
            marginRight: direction === "row" ? `${spacing}px` : "0px",
            marginTop: direction === "column" ? `${spacing}px` : "0px",
            marginBottom: direction === "column" ? `${spacing}px` : "0px",
        }}
        />   
    )
}

type SpacerProps = {
    spacing: number
    align?: SpacerAlign
    direction?: SpacerDirection
}

type SpacerAlign = "flex-start" | "flex-end" | AlignSetting
type SpacerDirection = 
| "row"
| "column"