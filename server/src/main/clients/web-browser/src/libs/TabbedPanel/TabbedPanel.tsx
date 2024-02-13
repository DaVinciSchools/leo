import 'swiper/css/bundle';

import {DetailedHTMLProps, HTMLAttributes, ReactNode, useEffect} from 'react';
import {Tab, TabProps, Tabs, TabsProps} from '@mui/material';
import {useURLSearchParam} from '../url_search_param';
import {DeepReadOnly} from '../misc';
import deepmerge from 'deepmerge';

export const TAB_PARAM_NAME = 'tab';

export function TabbedPanel(
  props: DeepReadOnly<{
    tabs: {key: number; label: ReactNode; content: ReactNode}[];
    tabKeyEnum: {[key: string | number]: string | number};
    defaultTabKey?: number | false;
    onTabKeyChange?: (key: number | false) => void;
  }> & {
    tabsProps?: TabsProps;
    tabProps?: TabProps;
    tabPanelStyle?: Partial<
      DetailedHTMLProps<HTMLAttributes<HTMLDivElement>, HTMLDivElement>
    >;
  }
) {
  const [activeTabKey, setActiveTabKey] = useURLSearchParam<number, false>(
    TAB_PARAM_NAME,
    props.defaultTabKey ?? false,
    value =>
      value === false
        ? undefined
        : value in props.tabKeyEnum
          ? (props.tabKeyEnum[value] as string)
          : undefined,
    param =>
      param in props.tabKeyEnum ? (props.tabKeyEnum[param] as number) : false
  );

  // Delay the call until the tab panel is visible.
  useEffect(() => {
    props.onTabKeyChange?.(activeTabKey);
  }, [activeTabKey]);

  return (
    <>
      <div
        style={{
          display: 'flex',
          flexFlow: 'column nowrap',
          height: '100%',
        }}
      >
        <Tabs
          // Convert between the tabs[value] to tab.key.
          value={props.tabs.findIndex(tab => tab.key === activeTabKey) ?? false}
          onChange={(_, newValue) =>
            setActiveTabKey(props.tabs.map(tab => tab.key)[newValue] ?? false)
          }
          scrollButtons="auto"
          {...props.tabsProps}
        >
          {props.tabs.map(tab => (
            <Tab
              key={tab.key as number}
              label={tab.label}
              {...props.tabProps}
            />
          ))}
        </Tabs>
        {props.tabs.map(tab => (
          <div
            {...deepmerge(
              {
                key: tab.key as number,
                hidden: activeTabKey !== tab.key,
                style: {height: '100%'},
              },
              props.tabPanelStyle ?? {}
            )}
          >
            {activeTabKey === tab.key && <>{tab.content}</>}
          </div>
        ))}
      </div>
    </>
  );
}
